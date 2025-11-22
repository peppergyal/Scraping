ThisBuild / scalaVersion := "3.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "fs2-scraper",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"  % "3.5.4",
      "co.fs2"        %% "fs2-core"     % "3.10.2",
      "co.fs2"        %% "fs2-io"       % "3.10.2",
      "org.http4s"    %% "http4s-ember-client" % "0.23.27",
      "org.jsoup"      % "jsoup"        % "1.18.3"
    )
  )
