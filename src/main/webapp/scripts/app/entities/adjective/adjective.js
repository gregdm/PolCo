'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adjective', {
                parent: 'entity',
                url: '/adjective',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adjective.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adjective/adjectives.html',
                        controller: 'AdjectiveController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adjective');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adjectiveDetail', {
                parent: 'entity',
                url: '/adjective/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adjective.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adjective/adjective-detail.html',
                        controller: 'AdjectiveDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adjective');
                        return $translate.refresh();
                    }]
                }
            });
    });
