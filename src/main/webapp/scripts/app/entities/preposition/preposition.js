'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('preposition', {
                parent: 'entity',
                url: '/preposition',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.preposition.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/preposition/prepositions.html',
                        controller: 'PrepositionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('preposition');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prepositionDetail', {
                parent: 'entity',
                url: '/preposition/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.preposition.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/preposition/preposition-detail.html',
                        controller: 'PrepositionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('preposition');
                        return $translate.refresh();
                    }]
                }
            });
    });
