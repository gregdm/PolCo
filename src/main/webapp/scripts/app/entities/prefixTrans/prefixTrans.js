'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prefixTrans', {
                parent: 'entity',
                url: '/prefixTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prefixTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefixTrans/prefixTranss.html',
                        controller: 'PrefixTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefixTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prefixTransDetail', {
                parent: 'entity',
                url: '/prefixTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prefixTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefixTrans/prefixTrans-detail.html',
                        controller: 'PrefixTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefixTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
