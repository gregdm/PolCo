'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adverb', {
                parent: 'entity',
                url: '/adverb',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adverb.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adverb/adverbs.html',
                        controller: 'AdverbController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adverb');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adverbDetail', {
                parent: 'entity',
                url: '/adverb/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.adverb.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/adverb/adverb-detail.html',
                        controller: 'AdverbDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('adverb');
                        return $translate.refresh();
                    }]
                }
            });
    });
