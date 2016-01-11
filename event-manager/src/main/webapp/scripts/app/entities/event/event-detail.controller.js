'use strict';

angular.module('eventmanagerApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event, Organizer) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('eventmanagerApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
