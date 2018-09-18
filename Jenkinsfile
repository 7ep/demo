pipeline {
  agent {
    docker {
      image 'gradle:alpine'
      args '-v /root/.m2:/root/.m2'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean compile'
      }
    }
  }
}
