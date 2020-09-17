heroku-run-local:
	echo 'Start deploying local heroku' &&  sbt clean compile stage && heroku local web
run:
	sbt run
