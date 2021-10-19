# MailMe
MailMe is a POP3 email client aimed for users that appreciate simplicity and privacy. 

## Requirements
- Java jdk 16
- Maven (optional)

## Obtaining the source
Clone the repository.
```
git clone https://github.com/hjernkrook/OOPP-Group77
```
Navigate to the repository and install dependencies using maven.
```
cd OOPP-Group77
mvn install 
```
Start the application.
```
mvn javafx:run
```
If autocomplete doesn't work, try.
```
mvn clean javafx:run
```

## Installation
There are currently no binaries available. 

## Usage
Currently the only supported email domain is gmail. To use your gmail account you need to allow [less secure apps](https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4Opn2VvV4M2FVc8GL4t5w2MV0dbOlMLrMpvHxL4yBg2BE5bOlubol8AT-zBCMExfS5rNOcaS4ehlx93WNOve30nCRPeNw),
because Oauth is not implemented yet.

## Package Structure
| Package              | Description                                                                  |
|----------------------|------------------------------------------------------------------------------|
| model/               | Contains all the data stuctures and buissness rules/logic.                   |
| controller/          | Contains multiple controllers for mediating events between model and view.   |
| services/            | Contains logic for persistent data storage and connecting to email servers.  |
| resoruces/           | Contains all views, stylingsheets, and images.                               | 
| test/                | Contains all the unit tests for the application.                             |
