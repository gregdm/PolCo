'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adjectiveTrans', {
                parent: 'entity',
                url: '/adjectiveTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adjectiveTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adjectiveTrans/adjectiveTranss.html',
                        controller: 'AdjectiveTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adjectiveTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adjectiveTransDetail', {
                parent: 'entity',
                url: '/adjectiveTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adjectiveTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adjectiveTrans/adjectiveTrans-detail.html',
                        controller: 'AdjectiveTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adjectiveTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
