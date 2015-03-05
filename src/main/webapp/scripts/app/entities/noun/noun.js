'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('noun', {
                parent: 'entity',
                url: '/noun',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.noun.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/noun/nouns.html',
                        controller: 'NounController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('noun');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nounDetail', {
                parent: 'entity',
                url: '/noun/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.noun.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/noun/noun-detail.html',
                        controller: 'NounDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('noun');
                        return $translate.refresh();
                    }]
                }
            });
    });
