const PROXY_CONFIG = [
  {
    context: [
      "/registration/"
    ],
    pathRewrite: {
      "^/registration/": "v1/onboardingservices/"
    },
    target: "http://localhost:8080/TGOCP-CM/",
    secure: false,
    "logLevel": "debug"
  }, {
    context: [
      "/*",
      "/*/*",
      "/*/*/",
      "/*/*/*",
      "/*/*/*/*",
      "/*/*/*/*/*",
      "/*/*/*/*/*/*"
    ]
    ,
    target: "http://localhost:8080/TGOCP-CM/",
    secure: false,
    "logLevel": "debug",
    "headers": {
      "x-ottg-caller-application": "yes",
      "x-ottg-caller-application-timestamp": "yes",
      "x-ottg-caller-application-host": "yes",
      "x-ottg-principal-userid": "yes",
      "IM_Login":"BAP_BYRJACK_02@ot.com",
      //"IM_UserID": "GCP860YO266A6",//Onboard
      "IM_BUID": "GC06101926UH",//959
      "IM_Login":"TUOnboarding@ot.com",
      "IM_UserID": "GCPG573FB1677",//Onboard
      "IM_BUID": "GC16689358OH",//959
      "IM_ServiceInstID": "929877",
      "IM_CommunityID": "GCM69516",
      "IM_FirstName": "User",
      "IM_LastName": "Mock",
      "IM_PreferredLanguage": "en",
      "IM_Country": "EN"
    }
  }]

module.exports = PROXY_CONFIG;
