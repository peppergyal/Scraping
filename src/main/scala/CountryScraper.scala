import cats.effect.*
import fs2.Stream
import org.http4s.*
import org.http4s.client.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.uri
import org.jsoup.Jsoup

import scala.jdk.CollectionConverters.*

object CountryScraper extends IOApp.Simple {

  val countriesUrl = uri"https://www.scrapethissite.com/pages/simple/"

  def fetchPage(client: Client[IO], url: Uri): IO[String] =
    client.expect[String](url).flatTap(html => IO.println(html.take(500)))

  def extractCountries(html: String): List[(String, String, String)] = {
    val doc = Jsoup.parse(html)
    // Each country is in div.country, with h3.country-name, span.country-capital, span.country-population
    doc.select("div.country").asScala.toList.map { div =>
      val name = div.select("h3.country-name").text()
      val capital = div.select("span.country-capital").text()
      val population = div.select("span.country-population").text()
      (name, capital, population)
    }
  }

  def saveCountry(c: (String, String, String)): IO[Unit] =
    IO.println(s"Country: ${c._1}, Capital: ${c._2}, Population: ${c._3}")

  def scraper(client: Client[IO]): Stream[IO, Unit] =
    Stream.emit(countriesUrl)
      .evalMap(fetchPage(client, _))
      .map(extractCountries)
      .flatMap(Stream.emits)
      .parEvalMap(10)(saveCountry)

  val run: IO[Unit] =
    EmberClientBuilder.default[IO].build.use { client =>
      scraper(client).compile.drain
    }
}
