// This jenkinsfile is used to run CI/CD on my local (Windows) box, no VM's needed.
pipeline {
  agent any
  tools {
    jdk 'Java 17'
    gradle 'Gradle 7.6'
  }

   environment {
        // This is set so that the Python API tests will recognize it
        // and go through the Zap proxy waiting at 9888
        HTTP_PROXY = 'http://127.0.0.1:9888'
        // Default Java Home for Jenkins (JDK 17)
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk'
        PATH = "${JAVA_HOME}/bin:${PATH}"
   }

  stages {

    stage('Docker Access Test') {
    steps {
      sh 'docker ps'
      }
    }
    // build the war file (the binary).  This is the only
    // place that happens.
    stage('Build') {
      environment {
        // Override JAVA_HOME to use JDK 11 for this stage
        JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
        PATH = "${JAVA_HOME}/bin:${PATH}"
      }
      steps {
        sh 'docker build -t ensf400project1 .'
      }
    }
    // run all the unit tests - these do not require anything else
    // to be running and most run very quickly.
    stage('Unit Tests') {
      environment {
        // Override JAVA_HOME to use JDK 11 for this stage
        JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
        PATH = "${JAVA_HOME}/bin:${PATH}"
      }
      steps {
        sh './gradlew test'
      }
      post {
        always {
          junit 'build/test-results/test/*.xml'
        }
      }
    }
    // // run the tests which require connection to a
    // // running database.
    // stage('Database Tests') {
    //   environment {
    //     // Override JAVA_HOME to use JDK 11 for this stage
    //     JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
    //     PATH = "${JAVA_HOME}/bin:${PATH}"
    //   }
    //   steps {
    //     sh './gradlew integrate'
    //   }
    //   post {
    //     always {
    //       junit 'build/test-results/integrate/*.xml'
    //     }
    //   }
    // }
    // // These are the Behavior Driven Development (BDD) tests
    // // See the files in src/bdd_test
    // // These tests do not require a running system.
    // stage('BDD Tests') {
    //   environment {
    //     // Override JAVA_HOME to use JDK 11 for this stage
    //     JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
    //     PATH = "${JAVA_HOME}/bin:${PATH}"
    //   }
    //   steps {
    //     sh './gradlew generateCucumberReports'
    //     // generate the code coverage report for jacoco
    //     sh './gradlew jacocoTestReport'
    //   }
    //   post {
    //       always {
    //         junit 'build/test-results/bdd/*.xml'
    //       }
    //     }
    // }
    // Runs an analysis of the code, looking for any
    // patterns that suggest potential bugs.
    stage('Static Analysis') {
      environment {
          // Override JAVA_HOME to use JDK 11 for this stage
          JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
          PATH = "${JAVA_HOME}/bin:${PATH}"
        }      

      steps{
        sh './gradlew sonarqube -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=ensf400'
          }
    }
  }
}