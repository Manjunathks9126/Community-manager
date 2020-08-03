#About 

This is Community Manager application which takes care of Community related functionalities


## How To Articles

## Running UI as standard alone (without deploying every time war in tomcat) 

	1.Run `ng serve --proxy-config uiproxy/localhost.config.js`
	2.localhost.config.js contanins REST calls proxy confiugration
	3.Add new path if existing paths are not enough
	4.Navigate to `http://localhost:4001/` to see the application. 
	Notes: 
		1.if there is not defualt routing i.e '', it shows error and needs to navigate particular routing 
		`http://localhost:4001/#<routing>` 
		2.The app will automatically reload if you change any of the source files.
		3. if you wan to run on different port 
		`ng serve --proxy-config uiproxy/localhost.config.js --port <portnumber>`

## Building the angular modules
	1. Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build
	2. When it goes higher environment it is already taken care in pom.xml