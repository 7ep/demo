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
        sh 'gradle clean assemble'
      }
    }

    stage('Test') {
      steps {
        sh 'gradle test'
      }
      post {
        always {
          junit 'build/test-results/test/*.xml'
        }
      }
    }

    stage('Deliver') {
      steps {
        sh './jenkins/scripts/deliver.sh'
      }

  }

}
