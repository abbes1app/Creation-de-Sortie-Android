#  Assistant événementiel de sorties

![Accueil application](/images/accueil.png)

L'objectif principal de cette application est de permettre **la création et l'organisation de sortie entre amis** de manière simple et automatique, tout en prenant en compte les contraintes liées aux adresses et aux préférences alimentaires des différents participants.  

# Prérequis

L'application a été développée pour les téléphones Android, de la version 4.0.3 (Ice cream sandwich) aux versions les plus récentes.

# Installation

## Compilation du code
* La compilation du code se fait obligatoirement à l'aide d'Android Studio qu'il faudra installer sur votre système (Linux, Mac ou Windows).
* Il faudrat ensuite aller dans File, Open..., et sélectionnez le dossier racine du projet préalablement téléchargé.
*  Vous pouvez maintenant créer une APK ou directement lancer l'application sur votre téléphone.

### a) Lancement direct de l'application
Pour cela, il vous faudra activer le mode "Débuggage" de votre téléphone (dans les paramètres, "Option de développement", cochez l'option "Débogage USB").

Si vous n'avez pas accès à la section "Option de développement", il faudra l'activer en allant dans "Parametres", "A propos de l'appareil", et cliquer plusieurs fois sur l'option "Numero de build" pour activer le mode développeur.

Une fois le mode débuggage USB activé, branchez votre téléphone sur l'ordinateur et allez dans "Run", "Run 'app'".
L'application sera installée et lancée automatiquement.

### b) Installation avec l'APK
Pour créer l'APK, il faudra (une fois le projet chargé dans Android Studio) aller dans "Build", "Build APK". Une fois terminé, il vous sera proposé d'ouvrir l'emplacement de l'APK que vous pourrez ensuite installer sur n'importe quel téléphone.

Il faudra penser à autoriser l'installation d'applications issues de sources inconnues en l'activant dans la rubrique " Sécurité " des paramètres du téléphone.

Ensuite, copiez le fichier .APK sur votre portable, utilisez un explorateur de fichier pour trouver le fichier .APK sur votre téléphone et cliquez sur ce dernier pour l'installer.


# Utilisation de l'application

Voici la marche à suivre pour créer une première sortie :

* Cliquez sur le bouton créer une sortie
* Indiquez le nom et le lieu de l'événement (pas obligatoire) que vous voulez organiser
* Définissez le moyen de transport
* Ajouter un ou plusieurs participants à la sortie (directement ou à partir de votre liste de contacts)
* Définir l'ordre des différentes étapes de la sortie
* Ajoutez des contraintes spécifiques pour coller aux goûts de tous
* Validez puis laissez à l'application le soin de vous indiquer le meilleur endroit pour la tenue de votre soirée.

# Outils utilisés

* [Android Studio](https://developer.android.com/studio/index.html) : IDE pour développement pour android
* Différentes API de Google (Places API Web Service, Google Map API, Distance Matrix API)
* [Icons8](https://icons8.com/) : Site utilisé pour trouver une grande partie des icones de l'application

# Auteurs

* **CHOUKCHOU Braham Abbes**
* **NDIAYE Thierno Ismaila**
* **VACHAUDEZ Cédric**
