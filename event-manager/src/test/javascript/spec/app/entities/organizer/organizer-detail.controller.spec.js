'use strict';

describe('Controller Tests', function() {

    describe('Organizer Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrganizer, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrganizer = jasmine.createSpy('MockOrganizer');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Organizer': MockOrganizer,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("OrganizerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'eventmanagerApp:organizerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
