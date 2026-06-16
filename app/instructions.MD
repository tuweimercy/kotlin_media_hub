### how to build an android app
1.Implement your project structure 
a. model: data storage references i.e determines our data properties
b. navigation: create routes ,pathways to different screens
c. UI : user interface screens - different interfaces for our users to interact with
- screens: actual components
- theme : colors,fonts to be used by app
2. adding dependencies build.garde (app module)
3. define screens and navgraph that will navigation to various screens
4. create the splashscreen : loader screen ,first screen show cased when the user opens our app i.e startdestinatiom
5. creating the authentication screen i.e regiseter ,login and forgot password screens
6. connecting project to firebase 
 - create the firebase
 - console.firebase.google.com
 - register the app in firebase console using the package name
 - download google-services.json config file 
 - add to your project /app folder for connection
 - enable the products needed -firebase auth 
 - firebase firestore and data storage
7. gradle and dependencies configs to add firebase to project
- build.gradle.kts(project,app )
WIRING THE AUTH PROCESS TO FIREBASE
1. Create the data model class to define necessary auth fields i.e. user profile 
2. Create the authviewmodel class and create necessary auth=fuctionality
A viewmodel is a bridge btwn our apps interface i.e views and its business logic.
Designed to prepare ,manage and hold ui related data in lifecycle independent of views
3. connect the view model functionalities to interfaces
   ### CRUD IMPLEMENTATION
 - Ensure firestore is enabled in firebase console
 - set up the model class for the colection upload
### Media Processing (images,videos and docs)
- use of cloudinary : add dependency to build.gradle
- make an application class for access to the service
- register the class in android manifest
- config cloudinary : set an upload preset in cloudinary
- set up helper function to upload files to cloudinary storage