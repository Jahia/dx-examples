angular.module('jahia.example')
  .directive('maSettings', ['maContextInfos', function (maContextInfos) {
    return {
      templateUrl: maContextInfos.moduleBase + "/javascript/example/settings/directives/settings/ma-settings.html",
      controller: ['$scope', 'maSettingsService', '$mdToast', '$mdDialog', 'i18nService',
        function ($scope, maSettingsService, $mdToast, $mdDialog, i18nService) {
          maSettingsService.getSettings().error(function () {
            console.debug("example: no settings yet")
          }).success(function (settingsData) {
            console.debug("getSettings: ", settingsData);
            if (settingsData) {
              $scope.settings = settingsData;
              console.debug("example: settings loaded");
            }
          });

          $scope.save = function () {
            var error = $scope.validate();

            if (error) {
              $scope._displayMsg(true, error);
              return;
            }

            maSettingsService.saveSettings($scope.settings)
              .success(function (settingsData) {
                console.debug("settingsData", settingsData);
                $scope.settings = settingsData;
                $scope._displayMsg(false, i18nService.message('angular.example.directives.settings.ma-settings.message.settingsSaved'));
              }).error(function (error) {
              if(error && error.type) {
                  if (error.error) {
                      $scope._displayMsg(true, error.error);
                  }
              }
            });
          };

          $scope.enable = function () {
            console.debug("enabled", $scope.settings.enabled);
          };

          $scope._displayMsg = function (isError, msg) {
            var toast = $mdToast.simple()
              .content(msg)
              .position("bottom right")
              .hideDelay(5000);
            if (isError) {
              toast.theme("alert");
            }
            $mdToast.show(toast);
          };

          $scope.validate = function () {

            if($scope.settings.enabled) {
              if(!$scope.settings.setting1) {
                  return i18nService.message('angular.example.directives.settings.ma-settings.validate.message.setting1');
              }

            }

            return false;
          };

          $scope.cardContentHeight = function () {
            var height = window.innerHeight - 250;
            return "height:" + height + "px;min-height: 400px;"
          }
        }]
    }
  }]);
