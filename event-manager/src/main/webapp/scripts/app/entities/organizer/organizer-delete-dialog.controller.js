'use strict';

angular.module('eventmanagerApp')
	.controller('OrganizerDeleteController', function($scope, $uibModalInstance, entity, Organizer) {

        $scope.organizer = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Organizer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
