Group Project - LetMeetHappen
Friends all like to meet but often, they need someone to step up to make a decision on where to meet and figure out common availability. This app will help in making a decision based on everyone's preferences on location, date, and other logistics.

Time Spent : XX hours spent in total

## User Stories

The following **required** functionality is completed:
* [x] The user should be able to login /signup for the app using google
* [x] The user should be able to create groups and add friends to them.  
* [x] Logged in User should be able to create meeting request for a group.
  * [x] User who generates a meeting request will enter the event name, event dates(max 2), group to invite, location, minimum yes needed from the group (min: 2, max: group size), deadline to respond to the invite 
  * [x] All member of the group should receive a push notification and email invite to repond to the meeting request
* [x] User should be able to respond(Yes/No/Maybe) to the meeting request
* [x] User can view all upcoming and past meetings on their home page.
  * [x] User can infinitely paginate both past and upcoming meetings
  * [x] Created meeting will moved to Pending Tab and when confirmed will be moved to Upcoming tab.
* [x] User can see their profile, group info, setting from the Navigation Drawer.
* [ ] User's get notifications when meeting is confirmed, cancelled or deadline approching.

The following **optional** features are implemented:

* [ ] User can save draft event invites and send them later. 
* [ ] User can login/sign up using facebook.
* [ ] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [ ] User can add **multiple location suggestions** to the meeting request
* [ ] User can **search for meeting matching a particular query** and see results
* [ ] On the day of the meeting display current location of all group member who will be attending few minutes before meeting time.
* [ ] Support time range for selected dates on the meeting invite.
* [ ] Add support to upload pictures to meetings and view them on the home screen.  

The following **bonus** features are implemented:

* [X] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [ ] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [ ] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks. 
* [ ] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [ ] For different meeting that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [ ] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit).

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!


## Video Walkthrough

Here's a walkthrough of implemented user stories:

## Week 7 DEMO :

<img src='https://i.imgur.com/3eKcJmp.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

Link to [Week 7 Demo](https://imgur.com/3eKcJmp).


## Week 6 DEMO Links :
Link to [Week 6 Demo1](https://i.imgur.com/xDnSmW7.gif).
 




## Week 5 -  DEMO

<img src='http://i.imgur.com/gSutrSX.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## Notes

Wireframe images are in PLAN/wireframes subfolder.

## Describe any challenges encountered while building the app.

Using Enums with  Model objects was not supported in Firebase.
Push Notifications 
Reading /writing from Firebase is complex 
Improve the way we add group member...incorporate some search mechanism




## Open-source libraries used
 
- Parceler 
- Firebase Database 
- Google / Firebase Auth 
- Retrolambda 
- Glide
- Butterknife


## License

    Copyright [2017] [Jennifer Godinez & Sonali Karwa]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

