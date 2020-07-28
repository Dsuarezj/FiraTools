heroku-run-local:
	echo 'Start deploying local heroku' &&  sbt compile stage && heroku local web
