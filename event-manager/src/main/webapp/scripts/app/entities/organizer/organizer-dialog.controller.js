'use strict';

angular.module('eventmanagerApp').controller('OrganizerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organizer', 'Event',
        function($scope, $stateParams, $uibModalInstance, entity, Organizer, Event) {

        $scope.organizer = entity;
        $scope.events = Event.query();
        $scope.load = function(id) {
            Organizer.get({id : id}, function(result) {
                $scope.organizer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('eventmanagerApp:organizerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.organizer.id != null) {
                Organizer.update($scope.organizer, onSaveSuccess, onSaveError);
            } else {
                Organizer.save($scope.organizer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
