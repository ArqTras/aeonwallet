<p align="center">
  <img src="Screenshot_20201125-085106_Aeon_Wallet[1].jpg" width="250" title="hover text">
</p>

# aeonwallet

An original android wallet app built for Aeon. Connect to a node, load your wallet, and send aeon.

## What is Aeon?

Aeon is a cryptocurrency based on the Cryptonote Protocol. It is intended to be a minimal version creating less encryption bloat and using a faster hashing algorithm KangarooTwelve while maintining the same private transactions and balances. Find more information https://github.com/aeonix/aeon.

## Download.

Find the apk files in https://github.com/ivoryguru/aeonwallet/tree/main/app/release. Most android phones will use the `app-arm64-v8a-release.apk`.

## Build instructions.

Some minor changes to aeon source code are necessary before compiling. Please see the changes https://github.com/ivoryguru/aeon/commits/master.

Build scripts require `ndk-r15c` in `/opt/android/`. See `env.sh`. https://developer.android.com/ndk/downloads/older_releases

Next, change directory to `external-libs` and run `sudo make install`. This will create the necessary lib files for the app.

After this is all completed, you will be able to open the app in Android Studio and build.


## How To Help.

Please report any issues using the app. Also, help translating and adding reliable nodes is appreciated. 

## Thanks.

Cheers to m2049r for the exemplary app xmrwallet and build scripts. https://github.com/m2049r/xmrwallet.
Also thanks to members of the Aeon community for their continued effort in realizing an ideal cryptocurrency.

## License

Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
