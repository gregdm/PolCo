'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prepositionTrans', {
                parent: 'entity',
                url: '/prepositionTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prepositionTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prepositionTrans/prepositionTranss.html',
                        controller: 'PrepositionTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prepositionTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prepositionTransDetail', {
                parent: 'entity',
                url: '/prepositionTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prepositionTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prepositionTrans/prepositionTrans-detail.html',
                        controller: 'PrepositionTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prepositionTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
