# How to release the library

## Configuration

### Configure repository credentials to Sonatype

Add the credentials to your Sonatype JIRA account to your `.m2/settings.xml` file:

```
<servers>
  <server>
    <id>ossrh</id>
    <username>JIRA-USERNAME</username>
  <password>JIRA-PASSWORD</password>
</server>
```

### Configure PGP for signing

Add the PGP configuration section to to your `.m2/settings.xml` file:

```
<profiles>
  <profile>
    <id>ossrh</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <gpg.executable>gpg</gpg.executable>
      <gpg.passphrase>YOUR-PASSPHRASE</gpg.passphrase>
    </properties>
  </profile>
</profiles>
```

You also need to have the PGP key stored with your PGP / GPG.

## Release

1) Set the version:
```
mvn versions:set -DnewVersion=1.1.0-SNAPSHOT
```

2) Deploy the release:
```
mvn clean javadoc:jar source:jar package gpg:sign deploy -P release
```

3) `SNAPSHOT` versions will go tot he snapshot repositories on Sonatype
4) Versions without `SNAPSHOT` will go to staging repository
5) Log into [https://oss.sonatype.org](https://oss.sonatype.org), close the staging repository and if it passes all checks release it