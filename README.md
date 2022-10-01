This is an extension for Android Studio I wrote in 2019, for enforcing green coding practices. It implements 12 detection rules and some quick fix thereof, based on an evolving [catalog of android-specific energy code smells](https://olegoaer.perso.univ-pau.fr/android-energy-smells/).

The successor of this prototype is now [ecoCode Mobile](https://github.com/cnumr/ecoCode/tree/main/src/android-plugin), based on SonarQube.

# 💾 Installation
* Download [greenchecks.jar]() (tested for Android Lint version 26.3.2)
* Move the file into your `~/.android/lint` directory. Create it if needed
* Restart Android Studio

# 🚀 Getting Started
* Open any Android project
* Select `Analyse>Inspect code` from the top menu
* Select your app Module only
* Focus on the new *Greenness* category reporting

![alt text](./assets/screenshot.png)

# 🫵 Contribute

In 2019 I waded in the undocumented waters of the Android Lint API but since 2021, there exist a documentation: http://googlesamples.github.io/android-custom-lint-rules/api-guide.html

For more information about recent changes in Android Lint and how to write custom Lint rules, please see the following links:
* Google group lint-dev: https://groups.google.com/g/lint-dev
* KotlinConf on youtube: https://youtu.be/p8yX5-lPS6o

# 🔗 How to cite this work?
```
@inproceedings{legoaer:hal-03252141,
  TITLE = {{Enforcing Green Code with Android Lint}},
  AUTHOR = {Le Goaer, Olivier},
  BOOKTITLE = {{ASEW '20: 35th IEEE/ACM International Conference on Automated Software Engineering Workshops}},
  ADDRESS = {Melbourne, Australia},
  PUBLISHER = {{ACM}},
  PAGES = {85-90},
  YEAR = {2021},
  MONTH = Sep,
  DOI = {10.1145/3417113.3422188},
  KEYWORDS = {green ; battery ; energy ; bugs ; lint ; smells ; Android},
}
```
