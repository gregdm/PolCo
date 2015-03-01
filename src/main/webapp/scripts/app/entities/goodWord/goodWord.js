'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('goodWord', {
                parent: 'entity',
                url: '/goodWord',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.goodWord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/goodWord/goodWords.html',
                        controller: 'GoodWordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('goodWord');
                        return $translate.refresh();
                    }]
                }
            })
            .state('goodWordDetail', {
                parent: 'entity',
                url: '/goodWord/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.goodWord.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/goodWord/goodWord-detail.html',
                        controller: 'GoodWordDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('goodWord');
                        return $translate.refresh();
                    }]
                }
            });
    });
