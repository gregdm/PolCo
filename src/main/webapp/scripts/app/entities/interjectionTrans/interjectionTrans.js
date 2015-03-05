'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('interjectionTrans', {
                parent: 'entity',
                url: '/interjectionTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.interjectionTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interjectionTrans/interjectionTranss.html',
                        controller: 'InterjectionTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('interjectionTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('interjectionTransDetail', {
                parent: 'entity',
                url: '/interjectionTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.interjectionTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interjectionTrans/interjectionTrans-detail.html',
                        controller: 'InterjectionTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('interjectionTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
