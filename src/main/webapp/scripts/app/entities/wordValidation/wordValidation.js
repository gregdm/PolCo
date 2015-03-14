'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('wordValidation', {
                parent: 'entity',
                url: '/wordValidation',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.wordValidation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/wordValidation/wordValidations.html',
                        controller: 'WordValidationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('wordValidation');
                        return $translate.refresh();
                    }]
                }
            })
            .state('wordValidationDetail', {
                parent: 'entity',
                url: '/wordValidation/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.wordValidation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/wordValidation/wordValidation-detail.html',
                        controller: 'WordValidationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('wordValidation');
                        return $translate.refresh();
                    }]
                }
            });
    });
