Initialize project
$ npm init -y

Install libraries
$ npm i express jsonwebtoken dotenv

Install nodemon
- https://nodemon.io/
- will monitor for any changes in your source and automatically restart your server
$ npm --save-dev nodemon
-> add to scripts in package.json: "devStart": "nodemon server.js",

Install cors
$ npm install cors

Start application
$ npm run devStart

Create random secret value
$ node
> require('crypto').randomBytes(64).toString('hex')
