'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('verb', {
                parent: 'entity',
                url: '/verb',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verb.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verb/verbs.html',
                        controller: 'VerbController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verb');
                        return $translate.refresh();
                    }]
                }
            })
            .state('verbDetail', {
                parent: 'entity',
                url: '/verb/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verb.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verb/verb-detail.html',
                        controller: 'VerbDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verb');
                        return $translate.refresh();
                    }]
                }
            });
    });
