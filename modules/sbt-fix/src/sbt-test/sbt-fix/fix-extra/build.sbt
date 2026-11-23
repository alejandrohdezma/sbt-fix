val touchInput = inputKey[Unit]("Creates the file named by its argument")

val touchTask = taskKey[Unit]("Creates the `task-key-ran` file")

touchInput := IO.touch(baseDirectory.value / sbt.complete.DefaultParsers.spaceDelimited("<file>").parsed.head)

touchTask := IO.touch(baseDirectory.value / "task-key-ran")

fixExtra += touchInput.toTask(" input-task-ran")

fixExtra += touchTask

fixExtra += Def.task(IO.touch(baseDirectory.value / "inline-task-ran"))
