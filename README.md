## WordHunch ##

[Download latest available version](https://sourceforge.net/projects/wordhunch/files/WordHunch%20-%20v0.0.1.apk/download)

WordHunch is a word guessing game that you can play on your own or with other players across the web. It gives the user two alphabets to begin with and the user is to make as many words as possible which start with the two letters. For each word that is made by the user, points are allocated in accordance with how points are won in the game of scrabble - Each letter carries an associated point.

The catch is to accumulate as many points as possible before the timer runs out. To make things interesting, there are certain thresholds which when crossed in terms of points, grants users powerups to multiply & increase their points. Powerups such as Double word Score, Triple word score and Double total score are up for grabs. 

Each challenge that is given to the user in the form of two alphabets are retrieved from Collins dictionary. The app downloads more challenges everyday in the bacground when the device is on charge and idle. The app will cease to function without an active internet connection.

The application is under active development and parts or gameplay may change as the game changes shape. The first version of the game, v0.0.1 is up and downloadable.

## Change log ##

### v0.0.1 ###
#### Changes / Features ####
* First draft release of the app;
* Contains background processes to download and purge suggestions;
* Scoring mechanism works as intended;
* Every user input is compared against suggestions from the keyboard. Cannot cross reference accuracy at this time;

#### Known Issues ###
* A dummy notification pops up when the background service executes. Please ignore it;
* Sometimes, accurate words are not accepted as possible inputs. This happens when the word does not exist on the keyboard but may be correct. No mechanism to verify such words exist;

## Acknowledgements ##

* CollinsDictionary : All challenges provided in the app are retrieved from CollinsDitionary's open APIs.

## Open Source Libraries ##
* [RetroFit](https://github.com/square/retrofit)
* [OkHttp](https://github.com/square/okhttp)
* [LinearTimer](https://github.com/krtkush/LinearTimer)
* [GreenDao](https://github.com/greenrobot/greenDAO)
* [EventBus](https://github.com/greenrobot/EventBus)
* [SQLite Cipher](https://github.com/sqlcipher/sqlcipher)
* [Firebase Job Dispatcher](https://github.com/firebase/firebase-jobdispatcher-android)
