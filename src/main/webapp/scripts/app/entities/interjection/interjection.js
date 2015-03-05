'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('interjection', {
                parent: 'entity',
                url: '/interjection',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.interjection.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interjection/interjections.html',
                        controller: 'InterjectionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('interjection');
                        return $translate.refresh();
                    }]
                }
            })
            .state('interjectionDetail', {
                parent: 'entity',
                url: '/interjection/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.interjection.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/interjection/interjection-detail.html',
                        controller: 'InterjectionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('interjection');
                        return $translate.refresh();
                    }]
                }
            });
    });
