angular.module('jahia.example')
  .service('maSettingsService', ['$http', 'maContextInfos', function ($http, maContextInfos) {
    this.saveSettings = function (settings) {
      return $http.post(maContextInfos.settingsActionUrl, settings);
    };

    this.getSettings = function () {
      return $http.get(maContextInfos.settingsActionUrl);
    };
  }]);
