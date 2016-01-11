'use strict';

angular.module('eventmanagerApp')
    .controller('OrganizerDetailController', function ($scope, $rootScope, $stateParams, entity, Organizer, Event) {
        $scope.organizer = entity;
        $scope.load = function (id) {
            Organizer.get({id: id}, function(result) {
                $scope.organizer = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventmanagerApp:organizerUpdate', function(event, result) {
            $scope.organizer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
