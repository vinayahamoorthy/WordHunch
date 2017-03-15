## WordHunch ##

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
        
* [OkHttp](https://github.com/square/okhttp)

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

* [LinearTimer (GPL v3)](https://github.com/krtkush/LinearTimer)
* [GreenDao](https://github.com/greenrobot/greenDAO)

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
        
* [EventBus](https://github.com/greenrobot/EventBus)

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

* [SQLite Cipher](https://github.com/sqlcipher/sqlcipher)

        Copyright (c) 2016, ZETETIC LLC All rights reserved.

        Redistribution and use in source and binary forms, with or without modification, 
        are permitted provided that the following conditions are met: 
        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer. 
        * Redistributions in binary form must reproduce the above copyright notice, 
        this list of conditions and the following disclaimer in the documentation and/or
        other materials provided with the distribution. 
        * Neither the name of the ZETETIC LLC nor the names of its contributors may be 
        used to endorse or promote products derived from this software without specific prior 
        written permission.

        THIS SOFTWARE IS PROVIDED BY ZETETIC LLC ''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
        INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
        PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ZETETIC LLC BE LIABLE FOR ANY DIRECT,
        INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
        PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
        HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
        (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
        ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
        
* [Firebase Job Dispatcher](https://github.com/firebase/firebase-jobdispatcher-android)

        Copyright 2004 Firebase

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
