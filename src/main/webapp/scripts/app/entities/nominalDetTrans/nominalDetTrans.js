'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nominalDetTrans', {
                parent: 'entity',
                url: '/nominalDetTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nominalDetTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nominalDetTrans/nominalDetTranss.html',
                        controller: 'NominalDetTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nominalDetTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nominalDetTransDetail', {
                parent: 'entity',
                url: '/nominalDetTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nominalDetTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nominalDetTrans/nominalDetTrans-detail.html',
                        controller: 'NominalDetTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nominalDetTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
