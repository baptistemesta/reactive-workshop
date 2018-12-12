# reactive-workshop

## Presentation

There is 2 components

### `external-service`
It simulate an external (distant) rest service. *It should not be modified*.


It run on port `3004` by default that serves `comments`:

See `com.bonitasoft.reactiveworkshop.external.ExternalApplication` 

### `backend`

The backend to work on that run on port `8080`

## How to run

### `external-service`

run the `external-service` with command `./gradlew :external-service:bootRun`

it  will launch the spring boot application on `http://localhost:3004/`

try [this url to verify it works](http://localhost:3004/comments/last10)

### `backend`

run the `backend` with command `./gradlew :backend:bootRun`

it will serve the application on `http://localhost:8080/`

try [this url to verify it works](http://localhost:8080/artists)

In the current state it uses an embedded h2 database

## What to do

### Add a new api method that return one artist and its 10 last comments

it should be called on this url:
`http://localhost:8080/artist/{artistId}/comments`

and should return something like
```json
{ 
  "artistId": "dfjksl342f32",
  "arstistName": "Radiohead",
  "genre": "Rock",
  "comments": [
      {
        "userName": "johndoe",
        "comment": "Nice band!"
      },
      {
        "userName": "walterbates",
        "comment": "It rocks"
      }
  ]
}
```

### Add a new api method that return the 10 last comments and associated artists filtered by a genre name


it should be called on this url:
`http://localhost:8080/genre/{genre}/comments`

and should return something like
```json
[
      {
        "artistId": "dfjksl342f32"
        "arstistName": "Radiohead",
        "userName": "johndoe",
        "comment": "Nice band!"
      },
      {
        "artistId": "fdsklj39fsjl"
        "arstistName": "Archive",
        "userName": "walterbates",
        "comment": "It rocks"
      }
]
```

### Add a new api method that return a stream of comments and associated artists filtered by a genre name

it should be called on this url:
`http://localhost:8080/genre/{genre}/comments/stream`

and return an infinite stream of json like this:

```json
{
  "artistId": "dfjksl342f32"
  "arstistName": "Radiohead",
  "userName": "johndoe",
  "comment": "Nice band!"
}
{
  "artistId": "fdsklj39fsjl"
  "arstistName": "Archive",
  "userName": "walterbates",
   "comment": "It rocks"
}
```


### Bonus

Replace h2 database by mongo db and use reactive repository `ReactiveMongoRepository` instead of `JpaRepository` (for the class `com.bonitasoft.reactiveworkshop.repository.ArtistRepository`)


(must run a mongo in a docker using `docker run --name mongo -p 27017:27017 -d mongo:3.2`)




