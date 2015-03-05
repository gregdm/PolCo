'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nounTrans', {
                parent: 'entity',
                url: '/nounTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nounTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nounTrans/nounTranss.html',
                        controller: 'NounTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nounTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nounTransDetail', {
                parent: 'entity',
                url: '/nounTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nounTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nounTrans/nounTrans-detail.html',
                        controller: 'NounTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nounTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
