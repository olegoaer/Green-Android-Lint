# CustomLintRule
This custom Lint rule allow for the detection of class declaration in the code source, which the name is 3 or less character short.

## Version
This rule works with the current version of Android studio (05/14/2018)
Android Studio 3.1
Android Lint 26.1.2
Java JRE 1.8

## How to use
In order to make this rule work, you'll need to compile it with Android Studio's gradle tool, and put the resulting .jar (found at CustomLintRule/myCheck/build/libs/myCheck.jar) into the custom lint rule folder, located by default at ~/.android/lint/ on Unix, and at the %userprofile% directory on Windows.

## For more info
For more information about recent changes in Android Lint and how to write custom Lint rules, please see the following links:
* Google group lint-dev: https://groups.google.com/forum/#!forum/lint-dev
* KotlinConf on youtube: https://youtu.be/p8yX5-lPS6o

# Règle Lint Personnalisée
Cette règle Lint personnalisée permet la détection de nom de classes dans le code source, dont le nom fait trois caractères ou moins.

## Version
Cette règle fonctionne sur la version actuelle d'Android Studio (05/14/2018)
Android Studio 3.1
Android Lint 26.1.2
Java JRE 1.8

## Comment l'utiliser
Pour utilisercette règle, vous devez d'abord la compiler à l'aide de gradle, l'outil de compilation d'Android Studio.
* Une fois dans Android Studio, dans le terminal, entrez les deux commandes suivantes
  * gradlew clean
  * gradlew assemble
* Ensuite rendez vous dans le répertoire CustomLintRule/myCheck/build/libs/myCheck.jar
* déplacer le .jar dans the dossier des règles lint personnalisées, situé pardéfaut à
   * ~/.android/lint/ sous Unix
   * %userprofile% (commande **Run** sous Windows.

## Pour plus d'informations
Pour plus d'informations concernant les récentes mises à jour et sur comment écrire une règle lint personnalisée, je vous conseille les liens suivants:
* Google group lint-dev: https://groups.google.com/forum/#!forum/lint-dev
* KotlinConf on youtube: https://youtu.be/p8yX5-lPS6o
