'use strict';

angular.module('eventmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('organizer', {
                parent: 'entity',
                url: '/organizers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'eventmanagerApp.organizer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/organizer/organizers.html',
                        controller: 'OrganizerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('organizer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('organizer.detail', {
                parent: 'entity',
                url: '/organizer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'eventmanagerApp.organizer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/organizer/organizer-detail.html',
                        controller: 'OrganizerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('organizer');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Organizer', function($stateParams, Organizer) {
                        return Organizer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('organizer.new', {
                parent: 'organizer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/organizer/organizer-dialog.html',
                        controller: 'OrganizerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    title: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('organizer', null, { reload: true });
                    }, function() {
                        $state.go('organizer');
                    })
                }]
            })
            .state('organizer.edit', {
                parent: 'organizer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/organizer/organizer-dialog.html',
                        controller: 'OrganizerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Organizer', function(Organizer) {
                                return Organizer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('organizer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('organizer.delete', {
                parent: 'organizer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/organizer/organizer-delete-dialog.html',
                        controller: 'OrganizerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Organizer', function(Organizer) {
                                return Organizer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('organizer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
