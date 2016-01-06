 'use strict';

angular.module('eventmanagerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-eventmanagerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-eventmanagerApp-params')});
                }
                return response;
            }
        };
    });
