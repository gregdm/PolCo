'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prefix', {
                parent: 'entity',
                url: '/prefix',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prefix.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefix/prefixs.html',
                        controller: 'PrefixController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefix');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prefixDetail', {
                parent: 'entity',
                url: '/prefix/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.prefix.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefix/prefix-detail.html',
                        controller: 'PrefixDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefix');
                        return $translate.refresh();
                    }]
                }
            });
    });
