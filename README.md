##WordHunch##

WordHunch is a word guessing game that you can play on your own or with other players across the web. It gives the user two alphabets to begin with and the user is to make as many words as possible which start with the two letters. For each word that is made by the user, points are allocated in accordance with how points are won in the game of scrabble - Each letter carries an associated point.

The catch is to accumulate as may points as possible before the timer runs out. To make things interesting, there are certain thresholds which when crossed in terms of points, grants users powerups to multiply & increase their points. Powerups such as Double word Score, Triple word score and Double total score are up for grabs. 

Each challenge that is given to the user in the form of two alphabets are retrieved from Collins dictionary. The app downloads more challenges everyday in the bacground when the device is on charge and idle. The app will cease to function without an active internet connection.

The application is under active development and parts or gameplay may change as the game changes shape. The first version of the game, v0.0.1 is up and downloadable.

##Changlog##

###v0.0.1###
####Changes / Features####
* First draft release of the app;
* Contains background processes to download and purge suggestions;
* Scoring mechanism works as intended;
* Every user input is compared against suggestions from the keyboard. Cannot cross reference accuracy at this time;

####Known Issues###
* A dummy notification pops up when the background service executes. Please ignore it;
* Sometimes, accurate words are not accepted as possible inputs. This happens when the word does not exist on the keyboard but may be correct. No mechanism to verify such words exist;

##Open Source Libraries##
* RetroFit

        Copyright 2013 Square, Inc.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
        
* OkHttp

        Copyright 2016 Square, Inc.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.

* LinearTimer (GPL v3)
* GreenDao

        Copyright 2014 Markus Junginger, greenrobot.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
        
* EventBus

        Copyright 2012-16 Markus Junginger, greenrobot.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
