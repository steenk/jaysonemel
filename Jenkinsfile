pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Building with Maven'
        sh 'mvn clean package'
      }
    }

  }
}