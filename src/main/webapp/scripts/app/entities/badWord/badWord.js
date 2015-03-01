'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('badWord', {
                parent: 'entity',
                url: '/badWord',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.badWord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/badWord/badWords.html',
                        controller: 'BadWordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('badWord');
                        return $translate.refresh();
                    }]
                }
            })
            .state('badWordDetail', {
                parent: 'entity',
                url: '/badWord/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.badWord.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/badWord/badWord-detail.html',
                        controller: 'BadWordDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('badWord');
                        return $translate.refresh();
                    }]
                }
            });
    });
