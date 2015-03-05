'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('verbTrad', {
                parent: 'entity',
                url: '/verbTrad',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verbTrad.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verbTrad/verbTrads.html',
                        controller: 'VerbTradController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verbTrad');
                        return $translate.refresh();
                    }]
                }
            })
            .state('verbTradDetail', {
                parent: 'entity',
                url: '/verbTrad/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verbTrad.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verbTrad/verbTrad-detail.html',
                        controller: 'VerbTradDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verbTrad');
                        return $translate.refresh();
                    }]
                }
            });
    });
