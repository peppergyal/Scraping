import cats.effect.{IO, IOApp}

object TestCats extends IOApp.Simple:

  def run: IO[Unit] =
    IO.println("If you see this, cats-effect works!")

