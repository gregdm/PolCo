'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('verbTrans', {
                parent: 'entity',
                url: '/verbTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verbTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verbTrans/verbTranss.html',
                        controller: 'VerbTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verbTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('verbTransDetail', {
                parent: 'entity',
                url: '/verbTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.verbTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/verbTrans/verbTrans-detail.html',
                        controller: 'VerbTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('verbTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
