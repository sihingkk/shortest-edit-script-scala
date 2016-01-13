name := "comparerro"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .configs( Benchmark, Functional)
  .settings( inConfig(Benchmark)(Defaults.testTasks) : _*)
  .settings( inConfig(Functional)(Defaults.testTasks) : _*)
  .settings(
    testOptions in Test := Seq(Tests.Filter(testFilter)),
    testOptions in Benchmark := Seq(Tests.Filter(benchmarkFilter)),
    testOptions in Functional := Seq(Tests.Filter(itTestFilter))
  )


def benchmarkFilter(name: String): Boolean = name endsWith "Benchmark"
def testFilter(name: String): Boolean = name endsWith "Test"
def itTestFilter(name: String): Boolean = name endsWith "Spec"


scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.specs2" % "specs2-scalacheck_2.11" % "2.4.16",
  "com.softwaremill.macwire" %% "macros" % "0.7.3",
  "com.softwaremill.macwire" %% "runtime" % "0.7",
  "com.yammer.metrics" % "metrics-core" % "2.1.2",
  "com.jayway.restassured" % "rest-assured" % "1.7" % "test"
)

lazy val Benchmark = config("benchmark") extend Test
lazy val Functional = config("fun") extend Test

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.6"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

logBuffered := false

parallelExecution in Benchmark := false
