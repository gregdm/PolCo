'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adverbTrans', {
                parent: 'entity',
                url: '/adverbTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adverbTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adverbTrans/adverbTranss.html',
                        controller: 'AdverbTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adverbTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adverbTransDetail', {
                parent: 'entity',
                url: '/adverbTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adverbTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adverbTrans/adverbTrans-detail.html',
                        controller: 'AdverbTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adverbTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
