# Fira tools

If you are making a fair, you might need tools that help do this more quick and efficient. 

## Pre requirements 

- Install [Heroku Toolbelt](https://toolbelt.heroku.com/).
- Install sbt
- Install play  
- Install java

## Running Locally  

```sh
$ git clone https://github.com/Dsuarezj/FiraTools.git
$ cd FiraTools
$ sbt compile stage
$ heroku local
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```
