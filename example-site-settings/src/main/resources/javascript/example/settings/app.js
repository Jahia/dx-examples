angular.module('jahia.example', ['ngMaterial', 'ngSanitize', 'ngRoute'])
  .config(function ($mdThemingProvider) {
    // theme used to create error toast
    $mdThemingProvider.theme('alert');
  });
