'use strict';

angular.module('eventmanagerApp')
    .factory('OrganizerSearch', function ($resource) {
        return $resource('api/_search/organizers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
