# LFI-Fuzzer-Burp-Suite
LFI-Fuzzer is a plugin for [Burp-Suite](https://portswigger.net/), this plugin can be used with the community edition to generate payloads for targets that could be vulnerable to local file inclusion attacks. 


![ConfigUI](https://i.imgur.com/8GJ0VdR.png)


![Intruder](https://i.imgur.com/9ne5boC.png)

## Getting Started

Go to Extender->Extensions and click in the Add button. Next select Java as the extension type and load in LfiBurp-1.0-jar-with-dependencies.jar

### Installing From Source

Make sure that you have maven installed and java. Then run the following to package the java. Note you will need an internet connection for the maven runtime dependencies.

```
git clone https://github.com/luke-goddard/LFI-Fuzzer-Burp-Suite
cd LFI-Fuzzer-Burp-Suite
mvn compile package 
file target/LfiBurp-1.0-jar-with-dependencies.jar
```

### Tested With

I have built the plugin using openjdk version "1.8.0_212". 



